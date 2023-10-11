package ru.practicum.ewm.event.specification;

import java.time.LocalDateTime;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.State;
import ru.practicum.ewm.event.dto.EventFiltersForPublic;

@Data
public class EventForPublicSpecification implements Specification<Event> {
    private final EventFiltersForPublic eventCriteria;

    @Override
    public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        Predicate predicate = criteriaBuilder
                .and(criteriaBuilder.equal(root.get("state"), State.PUBLISHED));

        if (eventCriteria.getText() != null) {
            Predicate annotationLikeText = criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")),
                    "%" + eventCriteria.getText().toLowerCase() + "%");

            Predicate descriptionLikeText = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                    "%" + eventCriteria.getText().toLowerCase() + "%");
            Predicate annotationAndDescription = criteriaBuilder.or(annotationLikeText, descriptionLikeText);

            predicate = criteriaBuilder.and(predicate, annotationAndDescription);
        }

        if (eventCriteria.getCategories() != null && !eventCriteria.getCategories().isEmpty()) {
            Join<Category, Event> category = root.join("category");
            predicate = criteriaBuilder.and(predicate, category.get("id").in(eventCriteria.getCategories()));
        }

        if (eventCriteria.getPaid() != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get("paid"), eventCriteria.getPaid()));
        }

        if (eventCriteria.getRangeStart() != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), eventCriteria.getRangeStart()));
        }

        if (eventCriteria.getRangeEnd() != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), eventCriteria.getRangeEnd()));
        }

        if (eventCriteria.getRangeStart() == null && eventCriteria.getRangeEnd() == null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.greaterThan(root.get("eventDate"), LocalDateTime.now()));
        }

        if (eventCriteria.isOnlyAvailable()) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.or(
                            criteriaBuilder.lessThan(root.get("confirmedRequests"), root.get("participantsLimit")),
                            criteriaBuilder.equal(root.get("participantsLimit"), 0)));
        }
        return predicate;
    }
}
