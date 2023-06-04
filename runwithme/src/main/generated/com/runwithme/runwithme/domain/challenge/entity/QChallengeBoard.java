package com.runwithme.runwithme.domain.challenge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QChallengeBoard is a Querydsl query type for ChallengeBoard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChallengeBoard extends EntityPathBase<ChallengeBoard> {

    private static final long serialVersionUID = 1073022064L;

    public static final QChallengeBoard challengeBoard = new QChallengeBoard("challengeBoard");

    public final StringPath challengeBoardContent = createString("challengeBoardContent");

    public final DateTimePath<java.time.LocalDateTime> challengeBoardRegTime = createDateTime("challengeBoardRegTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> challengeSeq = createNumber("challengeSeq", Long.class);

    public final NumberPath<Long> imgSeq = createNumber("imgSeq", Long.class);

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final NumberPath<Long> userSeq = createNumber("userSeq", Long.class);

    public QChallengeBoard(String variable) {
        super(ChallengeBoard.class, forVariable(variable));
    }

    public QChallengeBoard(Path<? extends ChallengeBoard> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChallengeBoard(PathMetadata metadata) {
        super(ChallengeBoard.class, metadata);
    }

}

