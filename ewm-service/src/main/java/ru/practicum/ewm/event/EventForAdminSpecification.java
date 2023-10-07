package ru.practicum.ewm.event;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.event.dto.EventFiltersForAdmin;
import ru.practicum.ewm.user.User;

@Data
public class EventForAdminSpecification implements Specification<Event> {
    private final EventFiltersForAdmin eventCriteria;

    @Override
    public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.and();

        if (eventCriteria.getUsers() != null) {
            Join<User, Event> initiator = root.join("initiator");
            predicate = criteriaBuilder.and(predicate,
                    initiator.get("id").in(eventCriteria.getUsers()));
        }
        if (eventCriteria.getStates() != null) {
            predicate = criteriaBuilder.and(predicate, root.get("state").in(eventCriteria.getStates()));
        }
        if (eventCriteria.getCategories() != null) {
            Join<Category, Event> category = root.join("category");
            predicate = criteriaBuilder.and(predicate, category.get("id").in(eventCriteria.getCategories()));
        }
        if (eventCriteria.getRangeStart() != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), eventCriteria.getRangeStart()));
        }
        if (eventCriteria.getRangeEnd() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"),
                    eventCriteria.getRangeEnd()));
        }
        return predicate;
    }
}