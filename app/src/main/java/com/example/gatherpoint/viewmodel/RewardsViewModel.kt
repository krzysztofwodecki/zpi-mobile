package com.example.gatherpoint.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.gatherpoint.network.Model
import com.example.gatherpoint.network.Resource
import com.example.gatherpoint.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RewardsViewModel (application: Application): AndroidViewModel(application) {

    private val _reward = MutableLiveData<Resource<Model.Reward>>()
    val reward: LiveData<Resource<Model.Reward>> = _reward

    private val rewards = MutableLiveData<Resource<List<Model.Reward>>>()
    private val rewardsSearchQuery = MutableLiveData("")

    val rewardsList = Utils.mediator(rewards, rewardsSearchQuery).map {
        val rewardsResource = rewards.value ?: return@map null
        val rewardsSearchQuery = rewardsSearchQuery.value ?: ""

        if (rewardsResource is Resource.Success) {
            Resource.Success(rewardsResource.data?.filter {
                it.name.contains(rewardsSearchQuery, ignoreCase = true) }
            )
        } else {
            rewardsResource
        }
    }

    private val redeemedRewards = MutableLiveData<Resource<List<Model.Reward>>>()
    private val redeemedRewardsSearchQuery = MutableLiveData("")

    val redeemedRewardsList = Utils.mediator(redeemedRewards, redeemedRewardsSearchQuery).map {
        val redeemedRewardsResource = redeemedRewards.value ?: return@map null
        val redeemedRewardsSearchQuery = redeemedRewardsSearchQuery.value ?: ""

        if (redeemedRewardsResource is Resource.Success) {
            Resource.Success(redeemedRewardsResource.data?.filter {
                it.name.contains(redeemedRewardsSearchQuery, ignoreCase = true) }
            )
        } else {
            redeemedRewardsResource
        }
    }

    fun getRewardsList() {
        rewards.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            delay(3000)
            rewards.postValue(Resource.Success(RewardProvider.getRewardList()))
        }
    }

    fun setRewardsSearchQuery(query: String) {
        rewardsSearchQuery.value = query
    }

    fun getRedeemedRewardsList() {
        redeemedRewards.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            delay(3000)
            redeemedRewards.postValue(Resource.Success(RewardProvider.getRewardList()))
        }
    }

    fun setRedeemedRewardsSearchQuery(query: String) {
        redeemedRewardsSearchQuery.value = query
    }

    fun getRewardById(eventId: Long) {
        _reward.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            delay(3000)
            _reward.postValue(Resource.Success(RewardProvider.getRewardList()[0]))
        }
    }

    object RewardProvider {
        fun getRewardList(): List<Model.Reward> {
            return listOf(
                Model.Reward(
                    id = 2,
                    name = "Pakiet EkoOdkrywca",
                    description = "Zanurz się w zrównoważonym stylu życia z naszym ekskluzywnym Pakietem EkoOdkrywca! Odkryj tajemnice ekologicznego życia dzięki tej specjalnie dobranej kolekcji ekologicznych niespodzianek. Chociaż szczegóły nagrody są jeszcze tajemnicze, możesz spodziewać się uroczego zestawu zrównoważonych niespodzianek, które podniosą Twój ekologiczny styl życia. Bądźcie gotowi na wielkie odkrycia i przygotujcie się do wyruszenia w podróż ku bardziej zielonej przyszłości. Wasze zaangażowanie w pozytywną zmianę zasługuje na nagrodę, a Pakiet EkoOdkrywca stoi gotów, by to umożliwić!",
                    value = 200
                ),
                Model.Reward(
                    id = 2,
                    name = "Pakiet EkoOdkrywca",
                    description = "Zanurz się w zrównoważonym stylu życia z naszym ekskluzywnym Pakietem EkoOdkrywca! Odkryj tajemnice ekologicznego życia dzięki tej specjalnie dobranej kolekcji ekologicznych niespodzianek. Chociaż szczegóły nagrody są jeszcze tajemnicze, możesz spodziewać się uroczego zestawu zrównoważonych niespodzianek, które podniosą Twój ekologiczny styl życia. Bądźcie gotowi na wielkie odkrycia i przygotujcie się do wyruszenia w podróż ku bardziej zielonej przyszłości. Wasze zaangażowanie w pozytywną zmianę zasługuje na nagrodę, a Pakiet EkoOdkrywca stoi gotów, by to umożliwić!",
                    value = 200
                ),
                Model.Reward(
                    id = 2,
                    name = "Pakiet EkoOdkrywca",
                    description = "Zanurz się w zrównoważonym stylu życia z naszym ekskluzywnym Pakietem EkoOdkrywca! Odkryj tajemnice ekologicznego życia dzięki tej specjalnie dobranej kolekcji ekologicznych niespodzianek. Chociaż szczegóły nagrody są jeszcze tajemnicze, możesz spodziewać się uroczego zestawu zrównoważonych niespodzianek, które podniosą Twój ekologiczny styl życia. Bądźcie gotowi na wielkie odkrycia i przygotujcie się do wyruszenia w podróż ku bardziej zielonej przyszłości. Wasze zaangażowanie w pozytywną zmianę zasługuje na nagrodę, a Pakiet EkoOdkrywca stoi gotów, by to umożliwić!",
                    value = 200
                ),
                Model.Reward(
                    id = 2,
                    name = "Pakiet EkoOdkrywca",
                    description = "Zanurz się w zrównoważonym stylu życia z naszym ekskluzywnym Pakietem EkoOdkrywca! Odkryj tajemnice ekologicznego życia dzięki tej specjalnie dobranej kolekcji ekologicznych niespodzianek. Chociaż szczegóły nagrody są jeszcze tajemnicze, możesz spodziewać się uroczego zestawu zrównoważonych niespodzianek, które podniosą Twój ekologiczny styl życia. Bądźcie gotowi na wielkie odkrycia i przygotujcie się do wyruszenia w podróż ku bardziej zielonej przyszłości. Wasze zaangażowanie w pozytywną zmianę zasługuje na nagrodę, a Pakiet EkoOdkrywca stoi gotów, by to umożliwić!",
                    value = 200
                ),
                Model.Reward(
                    id = 2,
                    name = "Pakiet EkoOdkrywca",
                    description = "Zanurz się w zrównoważonym stylu życia z naszym ekskluzywnym Pakietem EkoOdkrywca! Odkryj tajemnice ekologicznego życia dzięki tej specjalnie dobranej kolekcji ekologicznych niespodzianek. Chociaż szczegóły nagrody są jeszcze tajemnicze, możesz spodziewać się uroczego zestawu zrównoważonych niespodzianek, które podniosą Twój ekologiczny styl życia. Bądźcie gotowi na wielkie odkrycia i przygotujcie się do wyruszenia w podróż ku bardziej zielonej przyszłości. Wasze zaangażowanie w pozytywną zmianę zasługuje na nagrodę, a Pakiet EkoOdkrywca stoi gotów, by to umożliwić!",
                    value = 200
                ),
                Model.Reward(
                    id = 2,
                    name = "Pakiet EkoOdkrywca",
                    description = "Zanurz się w zrównoważonym stylu życia z naszym ekskluzywnym Pakietem EkoOdkrywca! Odkryj tajemnice ekologicznego życia dzięki tej specjalnie dobranej kolekcji ekologicznych niespodzianek. Chociaż szczegóły nagrody są jeszcze tajemnicze, możesz spodziewać się uroczego zestawu zrównoważonych niespodzianek, które podniosą Twój ekologiczny styl życia. Bądźcie gotowi na wielkie odkrycia i przygotujcie się do wyruszenia w podróż ku bardziej zielonej przyszłości. Wasze zaangażowanie w pozytywną zmianę zasługuje na nagrodę, a Pakiet EkoOdkrywca stoi gotów, by to umożliwić!",
                    value = 200
                ),
                Model.Reward(
                    id = 2,
                    name = "Pakiet EkoOdkrywca",
                    description = "Zanurz się w zrównoważonym stylu życia z naszym ekskluzywnym Pakietem EkoOdkrywca! Odkryj tajemnice ekologicznego życia dzięki tej specjalnie dobranej kolekcji ekologicznych niespodzianek. Chociaż szczegóły nagrody są jeszcze tajemnicze, możesz spodziewać się uroczego zestawu zrównoważonych niespodzianek, które podniosą Twój ekologiczny styl życia. Bądźcie gotowi na wielkie odkrycia i przygotujcie się do wyruszenia w podróż ku bardziej zielonej przyszłości. Wasze zaangażowanie w pozytywną zmianę zasługuje na nagrodę, a Pakiet EkoOdkrywca stoi gotów, by to umożliwić!",
                    value = 200
                ),
                Model.Reward(
                    id = 2,
                    name = "Pakiet EkoOdkrywca",
                    description = "Zanurz się w zrównoważonym stylu życia z naszym ekskluzywnym Pakietem EkoOdkrywca! Odkryj tajemnice ekologicznego życia dzięki tej specjalnie dobranej kolekcji ekologicznych niespodzianek. Chociaż szczegóły nagrody są jeszcze tajemnicze, możesz spodziewać się uroczego zestawu zrównoważonych niespodzianek, które podniosą Twój ekologiczny styl życia. Bądźcie gotowi na wielkie odkrycia i przygotujcie się do wyruszenia w podróż ku bardziej zielonej przyszłości. Wasze zaangażowanie w pozytywną zmianę zasługuje na nagrodę, a Pakiet EkoOdkrywca stoi gotów, by to umożliwić!",
                    value = 200
                ),
            )
        }
    }

}