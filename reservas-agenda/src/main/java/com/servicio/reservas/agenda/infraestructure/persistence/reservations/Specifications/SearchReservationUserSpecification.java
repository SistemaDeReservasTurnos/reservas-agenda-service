package com.servicio.reservas.agenda.infraestructure.persistence.reservations.Specifications;

import com.servicio.reservas.agenda.domain.entities.Reservation;
import com.servicio.reservas.agenda.infraestructure.persistence.reservations.ReservationModel;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SearchReservationUserSpecification implements Specification<ReservationModel> {

    private Long userId;
    private LocalDate from;
    private LocalDate to;
    private String status;


    @Override
    public Predicate toPredicate(Root<ReservationModel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        query.orderBy(criteriaBuilder.desc(root.get("date")));

        List<Predicate> predicates = new ArrayList<>();

        if(userId != null && userId != 0 ) {

            Predicate userIdPredicate = criteriaBuilder.equal(root.get("userId"), userId);
            predicates.add(userIdPredicate);

        }
        if(from != null ) {

            Predicate fromGreaterThanPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("date"), from);
            predicates.add(fromGreaterThanPredicate);
        }
        if(to != null ) {

            Predicate toLessThanPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("date"), to);
            predicates.add(toLessThanPredicate);
        }
        if(StringUtils.hasText(status)) {

            Expression<String> statusToLowerCase = criteriaBuilder.lower(root.get("status"));
            Predicate statusLikePredicate = criteriaBuilder.like(statusToLowerCase, "%".concat(status.toLowerCase()).concat("%"));
            predicates.add(statusLikePredicate);
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    public SearchReservationUserSpecification(Long userId, LocalDate from, LocalDate to, String status) {
        this.userId = userId;
        this.from = from;
        this.to = to;
        this.status = status;
    }
}
