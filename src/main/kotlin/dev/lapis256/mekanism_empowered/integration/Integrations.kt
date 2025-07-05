package dev.lapis256.mekanism_empowered.integration


object Integrations {
    private val integrations = listOf(
        EvoMek,
        MekElm,
        MekExt
    )

    fun initCommon() {
        integrations.forEach(IIntegration::initCommonIntegration)
    }

    fun initClient() {
        integrations.forEach(IIntegration::initClientIntegration)
    }
}
