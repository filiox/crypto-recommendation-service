package com.xm.crypto.recommendation.controller.delegate;

import com.xm.crypto.recommendation.api.CryptoRecommendationApiDelegate;
import com.xm.crypto.recommendation.controller.command.GetSortedByNormalizedRangeCommand;
import com.xm.crypto.recommendation.controller.command.GetCryptoStatsCommand;
import com.xm.crypto.recommendation.controller.command.GetHighestNormalizedRangeCommand;
import com.xm.crypto.recommendation.dto.CryptoHighestNormalizedRangeDto;
import com.xm.crypto.recommendation.dto.CryptoNormalizedRangeDto;
import com.xm.crypto.recommendation.dto.CryptoStatsDto;
import com.xm.crypto.recommendation.model.Request;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class CryptoRecommendationDelegate implements CryptoRecommendationApiDelegate {

    private final GetHighestNormalizedRangeCommand getHighestNormalizedRangeCommand;
    private final GetCryptoStatsCommand getCryptoStatsCommand;
    private final GetSortedByNormalizedRangeCommand getSortedByNormalizedRangeCommand;

    public CryptoRecommendationDelegate(GetHighestNormalizedRangeCommand getHighestNormalizedRangeCommand, GetCryptoStatsCommand getCryptoStatsCommand, GetSortedByNormalizedRangeCommand getSortedByNormalizedRangeCommand) {
        this.getHighestNormalizedRangeCommand = getHighestNormalizedRangeCommand;
        this.getCryptoStatsCommand = getCryptoStatsCommand;
        this.getSortedByNormalizedRangeCommand = getSortedByNormalizedRangeCommand;
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
    public ResponseEntity<CryptoHighestNormalizedRangeDto> getHighestNormalizedRange(String date){
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