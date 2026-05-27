package org.igirepay.igirepaypaymentgatewayproject.LAB2.dao;


public interface ProcessedRequestDAO {

    boolean isDuplicateReference(
            String referenceId
    );

    void saveReferenceId(
            String referenceId
    );
}
