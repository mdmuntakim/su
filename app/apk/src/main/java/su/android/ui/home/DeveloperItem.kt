package su.android.ui.home

import su.android.R
import su.android.core.Const
import su.android.databinding.RvItem
import su.android.core.R as CoreR

interface Dev {
    val name: String
}

private interface MUNTAKIMImpl : Dev {
    override val name get() = "iammuntakim"
}

sealed class DeveloperItem : Dev {

    abstract val items: List<IconLink>
    val handle get() = "@${name}"

    
    object iammuntakim: DeveloperItem(), MUNTAKIMImpl {
        override val items =
            listOf(
                object : IconLink.Github.User() { override val name = "iammuntakim" }
            )
    }
}

sealed class IconLink : RvItem() {

    abstract val icon: Int
    abstract val title: Int
    abstract val link: String

    override val layoutRes get() = R.layout.item_icon_link

    object Patreon : IconLink() {
        override val icon get() = CoreR.drawable.ic_patreon
        override val title get() = CoreR.string.patreon
        override val link get() = Const.Url.PATREON_URL
    }


    abstract class Github : IconLink() {
        override val icon get() = CoreR.drawable.ic_github
        override val title get() = CoreR.string.github

        abstract class User : Github(), Dev {
            override val link get() = "https://github.com/$name"
        }
    }

    object Source : IconLink() {
        override val icon get() = R.drawable.ic_code
        override val title get() = CoreR.string.github
        override val link get() = Const.Url.SOURCE_CODE_URL
    }
}