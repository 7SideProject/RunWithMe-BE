package com.runwithme.runwithme.domain.record.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecordCoordinate is a Querydsl query type for RecordCoordinate
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecordCoordinate extends EntityPathBase<RecordCoordinate> {

    private static final long serialVersionUID = 1247764810L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecordCoordinate recordCoordinate = new QRecordCoordinate("recordCoordinate");

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final QRunRecord runRecord;

    public final NumberPath<Long> Seq = createNumber("Seq", Long.class);

    public QRecordCoordinate(String variable) {
        this(RecordCoordinate.class, forVariable(variable), INITS);
    }

    public QRecordCoordinate(Path<? extends RecordCoordinate> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecordCoordinate(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecordCoordinate(PathMetadata metadata, PathInits inits) {
        this(RecordCoordinate.class, metadata, inits);
    }

    public QRecordCoordinate(Class<? extends RecordCoordinate> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.runRecord = inits.isInitialized("runRecord") ? new QRunRecord(forProperty("runRecord"), inits.get("runRecord")) : null;
    }

}

