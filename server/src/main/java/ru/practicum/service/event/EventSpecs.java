package ru.practicum.service.event;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.service.category.Category;
import ru.practicum.service.category.Category_;
import ru.practicum.service.request.Request;
import ru.practicum.service.request.Request_;
import ru.practicum.service.user.User;
import ru.practicum.service.user.User_;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.List;

public class EventSpecs {
    public static Specification<Event> hasInitiationIds(List<Long> userIds) {
        return (root, query, builder) -> {
            Join<Event, User> users = root.join(Event_.initiator, JoinType.LEFT);
            CriteriaBuilder.In<Long> inClause = builder.in(users.get(User_.id));
            if (userIds != null && userIds.size() != 0) {
                for (long userId : userIds) {
                    inClause.value(userId);
                }
            }

            return inClause;
        };
    }

    public static Specification<Event> hasEventCategory(List<Long> categories) {
        return (root, query, builder) -> {
            Join<Event, Category> categoryJoin = root.join(Event_.category, JoinType.LEFT);
            CriteriaBuilder.In<Long> inClause = builder.in(categoryJoin.get(Category_.id));
            if (categories != null && categories.size() != 0) {
                for (long categoryId : categories) {
                    inClause.value(categoryId);
                }
            }

            return inClause;
        };
    }

    public static Specification<Event> betweenDates(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart == null && rangeEnd == null) {
            return (root, query, builder) -> builder.greaterThan(root.get(Event_.eventDate), LocalDateTime.now());
        }

        if (rangeStart != null && rangeEnd == null) {
            return (root, query, builder) -> builder.greaterThan(root.get(Event_.eventDate), rangeStart);
        }

        if (rangeStart == null && rangeEnd != null) {
            return (root, query, builder) -> builder.between(root.get(Event_.eventDate), LocalDateTime.now(), rangeEnd);
        }

        return (root, query, builder) -> builder.between(root.get(Event_.eventDate), rangeStart, rangeEnd);

    }

    public static Specification<Event> hasTextInAnnotationOrDescription(String text) {
        return (root, query, builder) -> builder.or(
                builder.like(builder.lower(root.get(Event_.annotation)), "%" + text.toLowerCase() + "%"),
                builder.like(builder.lower(root.get(Event_.description)), "%" + text.toLowerCase() + "%")
        );
    }

    public static Specification<Event> isPaid(Boolean isPaid) {
        return (root, query, builder) -> {
            if (isPaid != null) {
                return builder.equal(root.get(Event_.paid), isPaid);
            }

            return null;
        };
    }

    public static Specification<Event> isEventAvailable(boolean isAvailable) {
        if (!isAvailable) {
            return null;
        }
        return (root, query, builder) -> {
            Subquery<Long> sub = query.subquery(Long.class);
            Root<Request> subRoot = sub.from(Request.class);
            Join<Request, Event> subEvents = subRoot.join(Request_.event.getName());
            sub.select(builder.count(subRoot.get(Request_.id)));
            sub.where(builder.equal(root.get(Event_.id), subEvents.get(Event_.id)));
            return builder.lessThan(sub, root.get(Event_.participantLimit));
        };
    }
}
