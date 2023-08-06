package com.runwithme.runwithme.domain.record.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRunRecord is a Querydsl query type for RunRecord
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRunRecord extends EntityPathBase<RunRecord> {

    private static final long serialVersionUID = 468921659L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRunRecord runRecord = new QRunRecord("runRecord");

    public final NumberPath<Long> avgSpeed = createNumber("avgSpeed", Long.class);

    public final NumberPath<Long> calorie = createNumber("calorie", Long.class);

    public final NumberPath<Long> challengeSeq = createNumber("challengeSeq", Long.class);

    public final ListPath<RecordCoordinate, QRecordCoordinate> coordinates = this.<RecordCoordinate, QRecordCoordinate>createList("coordinates", RecordCoordinate.class, QRecordCoordinate.class, PathInits.DIRECT2);

    public final StringPath endTime = createString("endTime");

    public final com.runwithme.runwithme.global.entity.QImage image;

    public final DatePath<java.time.LocalDate> regTime = createDate("regTime", java.time.LocalDate.class);

    public final NumberPath<Long> runningDistance = createNumber("runningDistance", Long.class);

    public final NumberPath<Long> runningTime = createNumber("runningTime", Long.class);

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final StringPath startTime = createString("startTime");

    public final NumberPath<Long> userSeq = createNumber("userSeq", Long.class);

    public QRunRecord(String variable) {
        this(RunRecord.class, forVariable(variable), INITS);
    }

    public QRunRecord(Path<? extends RunRecord> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRunRecord(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRunRecord(PathMetadata metadata, PathInits inits) {
        this(RunRecord.class, metadata, inits);
    }

    public QRunRecord(Class<? extends RunRecord> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.image = inits.isInitialized("image") ? new com.runwithme.runwithme.global.entity.QImage(forProperty("image")) : null;
    }

}

