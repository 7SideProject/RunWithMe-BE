package com.runwithme.runwithme.domain.challenge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChallengeBoard is a Querydsl query type for ChallengeBoard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChallengeBoard extends EntityPathBase<ChallengeBoard> {

    private static final long serialVersionUID = 1073022064L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChallengeBoard challengeBoard = new QChallengeBoard("challengeBoard");

    public final StringPath challengeBoardContent = createString("challengeBoardContent");

    public final DateTimePath<java.time.LocalDateTime> challengeBoardRegTime = createDateTime("challengeBoardRegTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> challengeSeq = createNumber("challengeSeq", Long.class);

    public final com.runwithme.runwithme.global.entity.QImage image;

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final com.runwithme.runwithme.domain.user.entity.QUser user;

    public QChallengeBoard(String variable) {
        this(ChallengeBoard.class, forVariable(variable), INITS);
    }

    public QChallengeBoard(Path<? extends ChallengeBoard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChallengeBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChallengeBoard(PathMetadata metadata, PathInits inits) {
        this(ChallengeBoard.class, metadata, inits);
    }

    public QChallengeBoard(Class<? extends ChallengeBoard> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.image = inits.isInitialized("image") ? new com.runwithme.runwithme.global.entity.QImage(forProperty("image")) : null;
        this.user = inits.isInitialized("user") ? new com.runwithme.runwithme.domain.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

