package org.mwangi.maya.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@Builder
public class DateTimeDTO {
    private LocalDateTime from;
    private LocalDateTime to;

    public DateTimeDTO() {
    }
}
