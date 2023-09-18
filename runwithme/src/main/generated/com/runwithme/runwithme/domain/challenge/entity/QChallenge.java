package com.runwithme.runwithme.domain.challenge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChallenge is a Querydsl query type for Challenge
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChallenge extends EntityPathBase<Challenge> {

    private static final long serialVersionUID = -397764586L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChallenge challenge = new QChallenge("challenge");

    public final BooleanPath checkYN = createBoolean("checkYN");

    public final NumberPath<Long> cost = createNumber("cost", Long.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> goalAmount = createNumber("goalAmount", Long.class);

    public final NumberPath<Long> goalDays = createNumber("goalDays", Long.class);

    public final StringPath goalType = createString("goalType");

    public final com.runwithme.runwithme.global.entity.QImage image;

    public final com.runwithme.runwithme.domain.user.entity.QUser manager;

    public final NumberPath<Long> maxMember = createNumber("maxMember", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Long> nowMember = createNumber("nowMember", Long.class);

    public final StringPath password = createString("password");

    public final DateTimePath<java.time.LocalDateTime> regTime = createDateTime("regTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final DatePath<java.time.LocalDate> timeEnd = createDate("timeEnd", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> timeStart = createDate("timeStart", java.time.LocalDate.class);

    public QChallenge(String variable) {
        this(Challenge.class, forVariable(variable), INITS);
    }

    public QChallenge(Path<? extends Challenge> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChallenge(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChallenge(PathMetadata metadata, PathInits inits) {
        this(Challenge.class, metadata, inits);
    }

    public QChallenge(Class<? extends Challenge> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.image = inits.isInitialized("image") ? new com.runwithme.runwithme.global.entity.QImage(forProperty("image")) : null;
        this.manager = inits.isInitialized("manager") ? new com.runwithme.runwithme.domain.user.entity.QUser(forProperty("manager"), inits.get("manager")) : null;
    }

}

