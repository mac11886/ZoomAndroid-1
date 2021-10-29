package us.zoom.sdksample.inmeetingfunction.customizedmeetingui;

import android.app.Activity;
import android.content.Context;

import java.util.List;

import us.zoom.sdk.IMeetingInviteMenuItem;
import us.zoom.sdk.ZoomUIDelegate;

public class SimpleZoomUIDelegate implements ZoomUIDelegate {
    @Override
    public boolean onClickInviteButton(Context context, List<IMeetingInviteMenuItem> inviteMenuList) {
        return false;
    }

    @Override
    public void afterMeetingMinimized(Activity activity) {

    }

    @Override
    public boolean onClickEndButton() {
        return false;
    }

    @Override
    public boolean onClickAudioButton() {
        return false;
    }

    @Override
    public boolean onClickVideoButton() {
        return false;
    }

    @Override
    public boolean onClickShareButton() {
        return false;
    }

    @Override
    public boolean onClickMoreButton() {
        return false;
    }

    @Override
    public boolean onClickParticipantsButton() {
        return false;
    }
}
