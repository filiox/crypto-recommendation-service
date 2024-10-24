package com.xm.crypto.recommendation.controller.command;

import com.xm.crypto.recommendation.dto.CryptoHighestNormalizedRangeDto;
import com.xm.crypto.recommendation.model.CryptoNormalizedRange;
import com.xm.crypto.recommendation.model.Request;
import com.xm.crypto.recommendation.service.CryptoRecommendationService;
import com.xm.crypto.recommendation.validator.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;

public class GetHighestNormalizedRangeCommand implements Command<Request, CryptoHighestNormalizedRangeDto>{

    private static final Logger logger = LoggerFactory.getLogger(GetHighestNormalizedRangeCommand.class);

    private final CryptoRecommendationService cryptoRecommendationService;

    public GetHighestNormalizedRangeCommand(CryptoRecommendationService cryptoRecommendationService) {
        this.cryptoRecommendationService = cryptoRecommendationService;
    }

    @Override
    public CryptoHighestNormalizedRangeDto execute(Request request) {
        logger.info("Getting highest normalized range for date: {}...", request.getForDate());
        LocalDate targetDate = DateUtils.getDatefromString(request.getForDate());
        CryptoHighestNormalizedRangeDto result = toDto(cryptoRecommendationService.getCryptoHighestNormalizedRangeForDate(targetDate));
        logger.info("Successfully retrieved highest normalized range for date: {}", request.getForDate());
        return result;
    }

    private CryptoHighestNormalizedRangeDto toDto(CryptoNormalizedRange cryptoNormalizedRange){
        logger.debug("Converting CryptoNormalizedRange to CryptoHighestNormalizedRangeDto for symbol: {}", cryptoNormalizedRange.getSymbol());
        CryptoHighestNormalizedRangeDto cryptoHighestNormalizedRangeDto = new CryptoHighestNormalizedRangeDto();
        cryptoHighestNormalizedRangeDto
                .normalizedRange(cryptoNormalizedRange.getNormalizedRange())
                .symbol(cryptoNormalizedRange.getSymbol());
        logger.debug("Successfully converted CryptoNormalizedRange to CryptoHighestNormalizedRangeDto for symbol: {}", cryptoNormalizedRange.getSymbol());
        return cryptoHighestNormalizedRangeDto;
    }
}