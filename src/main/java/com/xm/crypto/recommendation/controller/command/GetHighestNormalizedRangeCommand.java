package com.xm.crypto.recommendation.controller.command;

import com.xm.crypto.recommendation.dto.CryptoNormalizedRangeDto;
import com.xm.crypto.recommendation.model.CryptoNormalizedRange;
import com.xm.crypto.recommendation.model.Request;
import com.xm.crypto.recommendation.service.CryptoRecommendationService;
import com.xm.crypto.recommendation.validator.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

public class GetHighestNormalizedRangeCommand implements Command<Request, CryptoNormalizedRangeDto>{

    private static final Logger logger = LoggerFactory.getLogger(GetHighestNormalizedRangeCommand.class);

    private final CryptoRecommendationService cryptoRecommendationService;

    public GetHighestNormalizedRangeCommand(CryptoRecommendationService cryptoRecommendationService) {
        this.cryptoRecommendationService = requireNonNull(cryptoRecommendationService, "cryptoRecommendationService is required");
    }

    @Override
    public CryptoNormalizedRangeDto execute(Request request) {
        logger.info("Getting highest normalized range for date: {}...", request.getForDate());
        LocalDate targetDate = DateUtils.getDatefromString(request.getForDate());
        return toDto(cryptoRecommendationService.getHighestNormalizedRangeForDate(targetDate));
    }

    private CryptoNormalizedRangeDto toDto(CryptoNormalizedRange cryptoNormalizedRange){
        logger.debug("Converting CryptoNormalizedRange to CryptoHighestNormalizedRangeDto for symbol: {}", cryptoNormalizedRange.getSymbol());
        CryptoNormalizedRangeDto cryptoHighestNormalizedRangeDto = new CryptoNormalizedRangeDto();
        return cryptoHighestNormalizedRangeDto
                .normalizedRange(cryptoNormalizedRange.getNormalizedRange())
                .symbol(cryptoNormalizedRange.getSymbol());
    }
}