package com.runwithme.runwithme.domain.challenge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QChallenge is a Querydsl query type for Challenge
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChallenge extends EntityPathBase<Challenge> {

    private static final long serialVersionUID = -397764586L;

    public static final QChallenge challenge = new QChallenge("challenge");

    public final BooleanPath checkYN = createBoolean("checkYN");

    public final NumberPath<Long> cost = createNumber("cost", Long.class);

    public final DatePath<java.time.LocalDate> dateEnd = createDate("dateEnd", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> dateStart = createDate("dateStart", java.time.LocalDate.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> goalAmount = createNumber("goalAmount", Long.class);

    public final NumberPath<Long> goalDays = createNumber("goalDays", Long.class);

    public final StringPath goalType = createString("goalType");

    public final NumberPath<Long> imgSeq = createNumber("imgSeq", Long.class);

    public final NumberPath<Long> managerSeq = createNumber("managerSeq", Long.class);

    public final NumberPath<Long> maxMember = createNumber("maxMember", Long.class);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final DateTimePath<java.time.LocalDateTime> regTime = createDateTime("regTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final DateTimePath<java.time.LocalDateTime> timeEnd = createDateTime("timeEnd", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> timeStart = createDateTime("timeStart", java.time.LocalDateTime.class);

    public QChallenge(String variable) {
        super(Challenge.class, forVariable(variable));
    }

    public QChallenge(Path<? extends Challenge> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChallenge(PathMetadata metadata) {
        super(Challenge.class, metadata);
    }

}

