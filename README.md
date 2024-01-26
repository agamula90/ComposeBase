Jetpack compose sample with bottom bar navigation, side / navigation drawer navigation.

Bottom bar navigation consists of:
- 1st tab: dog list + dog details
- 2nd tab: dog list + dog place. On dog place screen mapbox sdk used to show dog location.
- 3rd tab: simple text

Bottom bar composable has custom clipping logic for animation when item clicked.

Side drawer consists of same 1st tab and mocked texts on 2nd and 3rd tabs.
Also side drawer contains fix for first draw pass offset issue on ModalNavDrawer, and synchronizes hamburger menu "morph" animation with drawer progress.

Exclusive features composable contains NoticeableRow composable that ensure that item at the end is always partially shown, so user understand that NoticeableRow is scrollable.