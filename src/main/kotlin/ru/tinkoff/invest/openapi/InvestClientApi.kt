package ru.tinkoff.invest.openapi

import ru.tinkoff.invest.openapi.apis.*

@Suppress("MemberVisibilityCanBePrivate")
class InvestClientApi(inSandboxMode: Boolean = false) {
    val market: MarketApi
    val operations: OperationsApi
    val orders: OrdersApi
    val portfolio: PortfolioApi
    val user: UserApi
    val sandbox: SandboxApi

    init {
        val basePath = if (inSandboxMode) tinkoffOpenApiSandboxUrl else tinkoffOpenApiUrl
        market = MarketApi(basePath)
        operations = OperationsApi(basePath)
        orders = OrdersApi(basePath)
        portfolio = PortfolioApi(basePath)
        user = UserApi(basePath)
        sandbox = SandboxApi(basePath)
    }

}
