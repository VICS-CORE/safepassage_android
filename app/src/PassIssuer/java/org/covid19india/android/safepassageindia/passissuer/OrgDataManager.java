package org.covid19india.android.safepassageindia.passissuer;

import java.util.ArrayList;
import java.util.List;

public class OrgDataManager {
    private static OrgDataManager ourInstance = null;

    private List<OrgUserInfo> mOrgUser = new ArrayList<>();

    public static OrgDataManager getInstance() {
        if(ourInstance == null) {
            ourInstance = new OrgDataManager();
            ourInstance.initializeOrgUser();
        }
        return ourInstance;
    }

    private void initializeOrgUser() {
        mOrgUser.add(initializeOrgUser1());
        mOrgUser.add(initializeOrgUser2());
    }

    public List<OrgUserInfo> getOrgUser(){ return mOrgUser; }

    private OrgUserInfo initializeOrgUser1() {
        UserInfo userInfo = new UserInfo("Udit","Bhargava","9686450991",UserInfo.UserType.ADMIN);
        OrgUserInfo orgUser= new OrgUserInfo(userInfo,"Senior");
        return orgUser;
    }
    private OrgUserInfo initializeOrgUser2() {
        UserInfo userInfo= new UserInfo("Ananya","Mallik","9632414466",UserInfo.UserType.ADMIN);
        OrgUserInfo orgUser= new OrgUserInfo(userInfo,"Senior");
        return orgUser;
    }
}
