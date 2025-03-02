package com.DTISE.ShelfMasterBE.usecase.report;

import com.DTISE.ShelfMasterBE.infrastructure.report.dto.GraphResponse;
import com.DTISE.ShelfMasterBE.infrastructure.report.dto.PopularProductResponse;
import com.DTISE.ShelfMasterBE.infrastructure.report.dto.SalesInfoCardResponse;

import java.time.LocalDate;
import java.util.List;

public interface GetDashboardDataUseCase {

    List<SalesInfoCardResponse> getSalesInfo(LocalDate date, Long warehouseId);

    List<PopularProductResponse> getThisMonthPopularProducts(LocalDate date, Long warehouseId);

    List<GraphResponse> getSalesGraphThisMonth(LocalDate date, Long warehouseId);

    List<GraphResponse> getSalesGraphThisYear(LocalDate date, Long warehouseId);

}
