package com.bdq.api.util;

import com.bdq.api.model.ValidationResponse;
import org.datakurator.ffdq.api.DQResponse;

import java.lang.reflect.Method;

/**
 * Utility to convert FFDQ DQResponse to the API's ValidationResponse.
 */
public final class FFDQMapper {

    private FFDQMapper() {}

    public static ValidationResponse toValidationResponse(DQResponse<?> dq) {
        if (dq == null) {
            return new ValidationResponse("INTERNAL_PREREQUISITES_NOT_MET", "", "No response from validator");
        }
        String status = dq.getResultState() != null ? dq.getResultState().getLabel() : "";
        String result = "";
        Object value = dq.getValue();
        if (value != null) {
            // Try common FFDQ value patterns without hard dependencies
            // 1) ComplianceValue: has getLabel()
            // 2) AmendmentValue/MeasurementValue: has getObject()
            // 3) Fallback to toString()
            try {
                Method getLabel = value.getClass().getMethod("getLabel");
                Object label = getLabel.invoke(value);
                if (label != null) {
                    result = String.valueOf(label);
                }
            } catch (ReflectiveOperationException noLabel) {
                try {
                    Method getObject = value.getClass().getMethod("getObject");
                    Object obj = getObject.invoke(value);
                    if (obj != null) {
                        result = String.valueOf(obj);
                    }
                } catch (ReflectiveOperationException noObject) {
                    result = String.valueOf(value);
                }
            }
        }
        String comment = dq.getComment();
        return new ValidationResponse(status, result, comment != null ? comment : "");
    }
}
