package ru.practicum.ewm.event;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

@Data
public class EventForAdminSpecification implements Specification<Event> {
    private final EventFiltersForAdmin eventCriteria;

    @Override
    public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.and();

        if (eventCriteria.getUsers() != null) {
            predicate = criteriaBuilder.and(predicate,
                    root.get("initiator_id").in(eventCriteria.getUsers()));
        }
        if (eventCriteria.getStates() != null) {
            predicate = criteriaBuilder.and(predicate, root.get("state").in(eventCriteria.getStates()));
        }
        if (eventCriteria.getCategories() != null) {
            predicate = criteriaBuilder.and(predicate, root.get("category_id").in(eventCriteria.getCategories()));
        }
        if (eventCriteria.getRangeStart() != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.greaterThanOrEqualTo(root.get("event_date"), eventCriteria.getRangeStart()));
        }
        if (eventCriteria.getRangeEnd() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("event_date"),
                    eventCriteria.getRangeEnd()));
        }
        return predicate;
    }
}