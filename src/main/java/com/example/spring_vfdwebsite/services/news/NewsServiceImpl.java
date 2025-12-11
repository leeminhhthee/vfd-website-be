package com.example.spring_vfdwebsite.services.news;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_vfdwebsite.annotations.LoggableAction;
import com.example.spring_vfdwebsite.dtos.newsDTOs.NewsCreateRequestDto;
import com.example.spring_vfdwebsite.dtos.newsDTOs.NewsResponseDto;
import com.example.spring_vfdwebsite.dtos.newsDTOs.NewsUpdateRequestDto;
import com.example.spring_vfdwebsite.dtos.searchDTOs.NewsIndexDto;
import com.example.spring_vfdwebsite.entities.News;
import com.example.spring_vfdwebsite.entities.User;
import com.example.spring_vfdwebsite.entities.enums.NewsStatusEnum;
import com.example.spring_vfdwebsite.events.news.NewsCreatedEvent;
import com.example.spring_vfdwebsite.events.news.NewsDeletedEvent;
import com.example.spring_vfdwebsite.events.news.NewsUpdatedEvent;
import com.example.spring_vfdwebsite.exceptions.EntityNotFoundException;
import com.example.spring_vfdwebsite.repositories.NewsJpaRepository;
import com.example.spring_vfdwebsite.repositories.UserJpaRepository;
import com.example.spring_vfdwebsite.utils.CloudinaryUtils;
import com.example.spring_vfdwebsite.utils.SlugUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class NewsServiceImpl implements NewsService {

    private final NewsJpaRepository newsRepository;
    private final UserJpaRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CloudinaryUtils cloudinaryUtils;
    private final SearchService searchService;
    private final TitleSummarizationService titleSummarizationService;

    // ===================== Get all =====================
    @Override
    @Cacheable(value = "news", key = "'all'")
    @Transactional(readOnly = true)
    public List<NewsResponseDto> getAllNews() {
        System.out.println("🔥 Fetching all news from the database...");
        List<News> newsList = newsRepository.findAllNews();
        return newsList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ===================== Get By Id =====================
    @Override
    @Cacheable(value = "news", key = "#root.args[0]")
    @Transactional(readOnly = true)
    public NewsResponseDto getNewsById(Integer id) {
        News news = newsRepository.findByIdNews(id)
                .orElseThrow(() -> new EntityNotFoundException("News with id " + id + " not found"));
        return toDto(news);
    }

    // ===================== Create =====================
    @Override
    @Transactional
    @CacheEvict(value = "news", allEntries = true)
    @LoggableAction(value = "CREATE", entity = "news", description = "Create a new news item")
    public NewsResponseDto createNews(NewsCreateRequestDto dto) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((org.springframework.security.core.userdetails.User) principal).getUsername();

        User currentUser = userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        String slug = generateUniqueSlug(dto.getTitle());

        News news = News.builder()
                .title(dto.getTitle())
                .slug(slug)
                .type(dto.getType())
                .content(dto.getContent())
                .imageUrl(dto.getImageUrl())
                .status(dto.getStatus())
                .authorBy(currentUser)
                .build();
        News savedNews = newsRepository.save(news);

        // Gọi AI để lấy tags (nếu có)
        List<String> generatedTags = titleSummarizationService.generateTags(savedNews.getContent());

        if (!generatedTags.isEmpty()) {
            String tagsForSql = String.join(", ", generatedTags);
            savedNews.setTags(tagsForSql);
        }

        NewsIndexDto indexDto = NewsIndexDto.builder()
                .id(savedNews.getId().toString())
                .title(savedNews.getTitle())
                .slug(savedNews.getSlug())
                .type(savedNews.getType())
                // Chỉ lấy 200 ký tự đầu của content để index cho nhẹ
                .content(savedNews.getContent().substring(0, Math.min(savedNews.getContent().length(), 200)))
                .imageUrl(savedNews.getImageUrl())
                .aiTags(generatedTags)
                .createdAt(savedNews.getCreatedAt())
                .build();

        searchService.addNewsToIndex(indexDto);

        // Publish event
        eventPublisher.publishEvent(new NewsCreatedEvent(savedNews.getId(), savedNews));

        return toDto(savedNews);
    }

    // ==================== Update =====================
    @Override
    @Transactional
    @CachePut(value = "news", key = "#p0")
    @CacheEvict(value = "news", allEntries = true)
    @LoggableAction(value = "UPDATE", entity = "news", description = "Update an existing news item")
    public NewsResponseDto updateNews(Integer id, NewsUpdateRequestDto dto) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("News with id " + id + " not found"));

        boolean titleChanged = false;

        if (dto.getTitle() != null && !dto.getTitle().equals(news.getTitle())) {
            news.setTitle(dto.getTitle());
            titleChanged = true;
        }
        if (dto.getType() != null)
            news.setType(dto.getType());
        if (dto.getContent() != null)
            news.setContent(dto.getContent());
        if (dto.getImageUrl() != null) {
            // Delete old image from Cloudinary and update to new one
            if (news.getImageUrl() != null) {
                cloudinaryUtils.deleteFile(news.getImageUrl());
            }
            news.setImageUrl(dto.getImageUrl());
        }
        if (dto.getStatus() != null)
            news.setStatus(dto.getStatus());

        boolean slugMissing = news.getSlug() == null || news.getSlug().isBlank();

        if (slugMissing) {
            news.setSlug(generateUniqueSlug(news.getTitle()));
        } else if (titleChanged && news.getStatus() == NewsStatusEnum.DRAFT) {
            news.setSlug(generateUniqueSlug(news.getTitle()));
        }

        News updatedNews = newsRepository.save(news);

        eventPublisher.publishEvent(new NewsUpdatedEvent(updatedNews.getId(), updatedNews));
        return toDto(updatedNews);
    }

    // ===================== Delete =====================
    @Override
    @Transactional
    @CacheEvict(value = "news", allEntries = true)
    @LoggableAction(value = "DELETE", entity = "news", description = "Delete an existing news item")
    public void deleteNews(Integer id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("News with id " + id + " not found"));

        if (news.getImageUrl() != null) {
            cloudinaryUtils.deleteFile(news.getImageUrl());
        }
        newsRepository.delete(news);
        eventPublisher.publishEvent(new NewsDeletedEvent(id));
    }

    // ===================== Get By Slug =====================
    @Override
    @Cacheable(value = "news", key = "#root.args[0]")
    @Transactional(readOnly = true)
    public NewsResponseDto getNewsBySlug(String slug) {
        News news = newsRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException("News with slug " + slug + " not found"));
        return toDto(news);
    }

    // ===================== Get By Id and Slug =====================
    @Override
    @Cacheable(value = "news", key = "'id:' + #root.args[0] + ':slug:' + #root.args[1]")
    @Transactional(readOnly = true)
    public NewsResponseDto getNewsByIdSlug(Integer id, String slug) {
        News news = newsRepository.findByIdAndSlug(id, slug)
                .orElseThrow(
                        () -> new EntityNotFoundException("News with id " + id + " and slug " + slug + " not found"));
        return toDto(news);
    }

    // ===================== Count News =====================
    @Override
    public long countNews() {
        return newsRepository.count();
    }

    // =================== Mapping -> Dto ===================
    private NewsResponseDto toDto(News news) {
        NewsResponseDto.AuthorByDto authorByDto = NewsResponseDto.AuthorByDto.builder()
                .id(news.getAuthorBy().getId())
                .fullName(news.getAuthorBy().getFullName())
                .email(news.getAuthorBy().getEmail())
                .imageUrl(news.getAuthorBy().getImageUrl())
                .build();
        return NewsResponseDto.builder()
                .id(news.getId())
                .title(news.getTitle())
                .slug(news.getSlug())
                .type(news.getType())
                .content(news.getContent())
                .imageUrl(news.getImageUrl())
                .status(news.getStatus())
                .tags(news.getTags())
                .authorBy(authorByDto)
                .createdAt(news.getCreatedAt())
                .updatedAt(news.getUpdatedAt())
                .build();
    }

    private String generateUniqueSlug(String title) {
        String baseSlug = SlugUtil.toSlug(title);
        String slug = baseSlug;
        int counter = 1;

        while (newsRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }
        return slug;
    }

}
