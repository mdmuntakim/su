package su.android.ui.home

import su.android.R
import su.android.core.Const
import su.android.databinding.RvItem
import su.android.core.R as CoreR

interface Dev {
    val name: String
}

private interface UkibatsuImpl : Dev {
    override val name get() = "muntakim"
}

sealed class DeveloperItem : Dev {

    abstract val items: List<IconLink>
    val handle get() = "@${name}"

    object Ukibatsu : DeveloperItem(), UkibatsuImpl {
        override val items = listOf(
            IconLink.TikTok,
            IconLink.YouTube,
            IconLink.GithubUser
        )
    }
}

sealed class IconLink : RvItem() {

    abstract val icon: Int
    abstract val title: Int
    abstract val link: String

    override val layoutRes get() = R.layout.item_icon_link

    object TikTok : IconLink() {
        override val icon get() = CoreR.drawable.ic_tiktok
        override val title get() = CoreR.string.tiktok
        override val link get() = "https://www.tiktok.com/@ukibatsu?_r=1&_t=ZS-96EUmT77gaF"
    }

    object YouTube : IconLink() {
        override val icon get() = CoreR.drawable.ic_youtube
        override val title get() = CoreR.string.youtube
        override val link get() = "https://youtube.com/@ukibatsu?si=2zAyBKpqKwbbJP1n"
    }

    object GithubUser : IconLink() {
        override val icon get() = CoreR.drawable.ic_github
        override val title get() = CoreR.string.github
        override val link get() = "https://github.com/iammuntakim"
    }
}