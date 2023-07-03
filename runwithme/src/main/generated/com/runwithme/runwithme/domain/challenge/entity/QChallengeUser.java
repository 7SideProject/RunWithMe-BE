package com.runwithme.runwithme.domain.challenge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChallengeUser is a Querydsl query type for ChallengeUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChallengeUser extends EntityPathBase<ChallengeUser> {

    private static final long serialVersionUID = 312278273L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChallengeUser challengeUser = new QChallengeUser("challengeUser");

    public final QChallenge challenge;

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final NumberPath<Long> userSeq = createNumber("userSeq", Long.class);

    public QChallengeUser(String variable) {
        this(ChallengeUser.class, forVariable(variable), INITS);
    }

    public QChallengeUser(Path<? extends ChallengeUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChallengeUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChallengeUser(PathMetadata metadata, PathInits inits) {
        this(ChallengeUser.class, metadata, inits);
    }

    public QChallengeUser(Class<? extends ChallengeUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.challenge = inits.isInitialized("challenge") ? new QChallenge(forProperty("challenge"), inits.get("challenge")) : null;
    }

}

