package flhan.de.financemanager.launcher

import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus.Success
import flhan.de.financemanager.base.RequestResult
import flhan.de.financemanager.common.UserSettings
import flhan.de.financemanager.launcher.LauncherState.Initialized
import flhan.de.financemanager.launcher.LauncherState.NotInitialized
import flhan.de.financemanager.login.AuthManager
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by fhansen on 06.10.17.
 */
interface CheckAuthInteractor {
    fun execute(): Observable<InteractorResult<LauncherState>>
}

class CheckAuthInteractorImpl @Inject constructor(
        private val authManager: AuthManager,
        private val userSettings: UserSettings)
    : CheckAuthInteractor {

    override fun execute(): Observable<InteractorResult<LauncherState>> {
        return userSettings
                .hasUserData()
                .toObservable()
                .flatMap { hasUserData ->
                    if (hasUserData) {
                        authManager.checkAuth()
                    } else {
                        Observable.just(RequestResult(false))
                    }
                }
                .map { authResult ->
                    if (authResult.result == true)
                        InteractorResult(Success, Initialized)
                    else
                        InteractorResult(Success, NotInitialized)
                }
    }
}
