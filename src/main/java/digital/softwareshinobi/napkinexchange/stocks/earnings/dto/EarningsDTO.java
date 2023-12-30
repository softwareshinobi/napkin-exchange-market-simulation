package digital.softwareshinobi.napkinexchange.stocks.earnings.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

import digital.softwareshinobi.napkinexchange.stocks.earnings.entity.EarningsReport;

@Getter
@Setter
public class EarningsDTO {

    private double estimatedEPS;
    private double actualEPS;
    private String reportMessage;
    private ZonedDateTime dateOfRelease;

    public EarningsDTO(EarningsReport earnings) {
        this.estimatedEPS = earnings.getEstimatedEPS();
        this.actualEPS = earnings.getActualEPS();
        this.reportMessage = earnings.getReportMessage();
        this.dateOfRelease = earnings.getDateOfRelease();
    }
}
