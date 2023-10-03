package ru.practicum.ewm.event;

import java.time.LocalDateTime;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

@Data
public class EventForPublicSpecification implements Specification<Event> {
    private final EventFiltersForPublic eventCriteria;

    @Override
    public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        Predicate predicate = criteriaBuilder
                .and(criteriaBuilder.equal(root.get("state"), "PUBLISHED"),
                        criteriaBuilder.equal(root.get("is_paid"), eventCriteria.isPaid()));

        if (eventCriteria.getText() != null) {
            Predicate annotationLikeText = criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")),
                    "%" + eventCriteria.getText().toLowerCase() + "%");

            Predicate descriptionLikeText = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                    "%" + eventCriteria.getText().toLowerCase() + "%");
            Predicate annotationAndDescription = criteriaBuilder.or(annotationLikeText, descriptionLikeText);

            predicate = criteriaBuilder.and(predicate, annotationAndDescription);
        }

        if (!eventCriteria.getCategories().isEmpty()) {
            predicate = criteriaBuilder.and(predicate, root.get("category_id").in(eventCriteria.getCategories()));
        }

        if (eventCriteria.getRangeStart() != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.greaterThanOrEqualTo(root.get("event_date"), eventCriteria.getRangeStart()));
        }

        if (eventCriteria.getRangeEnd() != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.lessThanOrEqualTo(root.get("event_date"), eventCriteria.getRangeEnd()));
        }

        if (eventCriteria.getRangeStart() == null && eventCriteria.getRangeEnd() == null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.greaterThan(root.get("event_date"), LocalDateTime.now()));
        }

        return predicate;
    }
}
