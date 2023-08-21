package com.runwithme.runwithme.domain.challenge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChallengeBoardWarn is a Querydsl query type for ChallengeBoardWarn
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChallengeBoardWarn extends EntityPathBase<ChallengeBoardWarn> {

    private static final long serialVersionUID = 2082886422L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChallengeBoardWarn challengeBoardWarn = new QChallengeBoardWarn("challengeBoardWarn");

    public final QChallengeBoard challengeBoard;

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final com.runwithme.runwithme.domain.user.entity.QUser user;

    public QChallengeBoardWarn(String variable) {
        this(ChallengeBoardWarn.class, forVariable(variable), INITS);
    }

    public QChallengeBoardWarn(Path<? extends ChallengeBoardWarn> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChallengeBoardWarn(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChallengeBoardWarn(PathMetadata metadata, PathInits inits) {
        this(ChallengeBoardWarn.class, metadata, inits);
    }

    public QChallengeBoardWarn(Class<? extends ChallengeBoardWarn> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.challengeBoard = inits.isInitialized("challengeBoard") ? new QChallengeBoard(forProperty("challengeBoard"), inits.get("challengeBoard")) : null;
        this.user = inits.isInitialized("user") ? new com.runwithme.runwithme.domain.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

