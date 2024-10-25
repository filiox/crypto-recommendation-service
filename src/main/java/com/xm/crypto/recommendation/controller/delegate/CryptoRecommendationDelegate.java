package com.xm.crypto.recommendation.controller.delegate;

import com.xm.crypto.recommendation.api.CryptoRecommendationApiDelegate;
import com.xm.crypto.recommendation.controller.command.GetSortedByNormalizedRangeCommand;
import com.xm.crypto.recommendation.controller.command.GetCryptoStatsCommand;
import com.xm.crypto.recommendation.controller.command.GetHighestNormalizedRangeCommand;
import com.xm.crypto.recommendation.dto.CryptoNormalizedRangeDto;
import com.xm.crypto.recommendation.dto.CryptoStatsDto;
import com.xm.crypto.recommendation.model.Request;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class CryptoRecommendationDelegate implements CryptoRecommendationApiDelegate {

    private final GetHighestNormalizedRangeCommand getHighestNormalizedRangeCommand;
    private final GetCryptoStatsCommand getCryptoStatsCommand;
    private final GetSortedByNormalizedRangeCommand getSortedByNormalizedRangeCommand;

    public CryptoRecommendationDelegate(GetHighestNormalizedRangeCommand getHighestNormalizedRangeCommand, GetCryptoStatsCommand getCryptoStatsCommand, GetSortedByNormalizedRangeCommand getSortedByNormalizedRangeCommand) {
        this.getHighestNormalizedRangeCommand = requireNonNull(getHighestNormalizedRangeCommand, "getHighestNormalizedRangeCommand must not be null");
        this.getCryptoStatsCommand = requireNonNull(getCryptoStatsCommand, "getCryptoStatsCommand must not be null");
        this.getSortedByNormalizedRangeCommand = requireNonNull(getSortedByNormalizedRangeCommand, "getSortedByNormalizedRangeCommand must not be null");
    }

    @Override
    public ResponseEntity<CryptoStatsDto> getCryptoStats(String symbol,
                                                         String startDate,
                                                         String endDate){
        return ResponseEntity.ok(getCryptoStatsCommand.execute(Request.builder()
                .symbol(symbol)
                .startDate(startDate)
                .endDate(endDate)
                .build()));
    }

    @Override
    public ResponseEntity<CryptoNormalizedRangeDto> getHighestNormalizedRange(String date){
        return ResponseEntity.ok(getHighestNormalizedRangeCommand.execute(Request.builder()
                .forDate(date)
                .build()));
    }

    @Override
    public ResponseEntity<List<CryptoNormalizedRangeDto>> getCryptoSortedByNormalizedRange(){
        return ResponseEntity.ok(getSortedByNormalizedRangeCommand.execute(Request.builder()
                .build()));
    }

}