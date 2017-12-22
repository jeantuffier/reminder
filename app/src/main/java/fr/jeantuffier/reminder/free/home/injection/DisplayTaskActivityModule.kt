package fr.jeantuffier.reminder.free.home.injection

import dagger.Module
import dagger.Provides
import fr.jeantuffier.reminder.free.common.injection.ActivityScope
import fr.jeantuffier.reminder.free.home.presentation.DisplayTaskModel
import fr.jeantuffier.reminder.free.home.presentation.ProvidedDisplayTaskPresenterOps
import fr.jeantuffier.reminder.free.home.presentation.HomePresenter
import fr.jeantuffier.reminder.free.home.presentation.HomeActivity
import java.lang.ref.WeakReference

/**
 * Created by jean on 25.10.2016.
 */
@Module
class DisplayTaskActivityModule(private val activity : HomeActivity) {

    @Provides
    @ActivityScope
    fun providesTaskListActivity() = activity

    @Provides
    @ActivityScope
    fun providesTaskListPresenter() : ProvidedDisplayTaskPresenterOps {
        val presenter = HomePresenter(WeakReference(activity))
        val model = DisplayTaskModel(presenter)
        presenter.model = model

        return presenter
    }

}