package info.nightscout.androidaps.plugins.pump.eopatch.vo

import com.google.common.base.Preconditions
import info.nightscout.androidaps.plugins.pump.eopatch.CommonUtils
import info.nightscout.androidaps.plugins.pump.eopatch.GsonHelper
import info.nightscout.androidaps.plugins.pump.eopatch.code.SettingKeys
import info.nightscout.androidaps.plugins.pump.eopatch.code.UnitOrPercent
import info.nightscout.shared.sharedPreferences.SP
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class TempBasalManager : IPreference<TempBasalManager>{
    @Transient
    private val subject: BehaviorSubject<TempBasalManager> = BehaviorSubject.create()

    var startedBasal: TempBasal? = null

    private var startTimestamp = 0L

    private var endTimestamp = 0L

    var unit = UnitOrPercent.P

    init {

    }


    fun clear(){
        startedBasal = null
        startTimestamp = 0L
        endTimestamp = 0L
    }

    fun updateBasalRunning(tempBasal: TempBasal) {
        Preconditions.checkNotNull(tempBasal)

        this.startedBasal = CommonUtils.clone(tempBasal)
        this.startedBasal?.running = true
    }


    /**
     * 특정 베이젤의 인덱스 찾기
     *
     * @param basal
     * @return
     */


    fun updateBasalStopped() {
        // 모두 정지
        this.startedBasal?.running = false
        this.startedBasal?.startTimestamp = 0
        // subject.onNext(this)
    }

    fun updateForDeactivation() {
        // deactivation할때는 모두 정지
        updateBasalStopped()
        // subject.onNext(this)
    }



    fun updateDeactivation() {
        updateBasalStopped()
    }



    fun update(other: TempBasalManager){
        this.startedBasal = other.startedBasal
        startTimestamp = other.startTimestamp
        endTimestamp = other.endTimestamp
        unit = other.unit
    }

    override fun observe(): Observable<TempBasalManager> {
        return subject.hide()
    }

    override fun flush(sp: SP){
        val jsonStr = GsonHelper.sharedGson().toJson(this)
        sp.putString(SettingKeys.TEMP_BASAL, jsonStr)
        subject.onNext(this)
    }

    override fun toString(): String {
        return "TempBasalManager(startedBasal=$startedBasal, startTimestamp=$startTimestamp, endTimestamp=$endTimestamp, unit=$unit)"
    }

    companion object {

        const val NAME = "TEMP_BASAL_MANAGER"

        val MAX_BASAL_SEQ = 20
        val MANUAL_BASAL_SEQ = MAX_BASAL_SEQ + 1
    }

}