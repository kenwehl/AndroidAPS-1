package info.nightscout.androidaps.plugins.pump.common.sync

import info.nightscout.androidaps.data.DetailedBolusInfo
import info.nightscout.androidaps.plugins.pump.common.defs.PumpType


data class PumpDbEntry constructor(var temporaryId: Long,
                       var date: Long,
                       var pumpType: PumpType,
                       var serialNumber: String,
                       var bolusData: PumpDbEntryBolus? = null,
                       var tbrData: PumpDbEntryTBR? = null ) {

    constructor(temporaryId: Long,
                date: Long,
                pumpType: PumpType,
                serialNumber: String,
                detailedBolusInfo: DetailedBolusInfo) : this(temporaryId, date, pumpType, serialNumber) {
        this.bolusData = PumpDbEntryBolus(
            detailedBolusInfo.insulin,
            detailedBolusInfo.carbs,
            detailedBolusInfo.bolusType)
    }
}


data class PumpDbEntryBolus(var insulin: Double,
                            var carbs: Double,
                            var bolusType: DetailedBolusInfo.BolusType)

data class PumpDbEntryCarbs(var date: Long,
                            var carbs: Double,
                            var pumpType: PumpType,
                            var serialNumber: String,
                            var pumpId: Long? = null) {
    constructor(detailedBolusInfo: DetailedBolusInfo,
                creator: PumpSyncEntriesCreator) : this(detailedBolusInfo.timestamp,
                                                        detailedBolusInfo.carbs,
                                                        creator.model(),
                                                        creator.serialNumber())
}

data class PumpDbEntryTBR(var temporaryId: Long)