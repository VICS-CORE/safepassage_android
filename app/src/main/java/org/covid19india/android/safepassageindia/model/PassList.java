package org.covid19india.android.safepassageindia.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PassList {
    @SerializedName("userpass")
    private List<Pass> passes;

    public PassList(List<Pass> passes) {
        this.passes = passes;
    }

    public List<Pass> getPasses() {
        return passes;
    }

    public void setPasses(List<Pass> passes) {
        this.passes = passes;
    }

    public void renamePassType() {
        for (Pass pass : passes) {
            if (pass.getPass_type().equals("O")) {
                pass.setPass_type("One Time");
            } else if (pass.getPass_type().equals("P")) {
                pass.setPass_type("Permanent");
            } else if (pass.getPass_type().equals("T")) {
                pass.setPass_type("Temporary");
            }
        }
    }
}
