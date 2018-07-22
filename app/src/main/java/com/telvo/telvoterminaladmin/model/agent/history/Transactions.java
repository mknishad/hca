package com.telvo.telvoterminaladmin.model.agent.history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by invar on 08-Nov-17.
 */

public class Transactions implements Serializable {
    @SerializedName("deposits")
    @Expose
    private List<Deposit> deposits = null;
    @SerializedName("withdraws")
    @Expose
    private List<Withdraw> withdraws = null;
    @SerializedName("ownDeposits")
    @Expose
    private List<OwnDeposit> ownDeposits = null;
    @SerializedName("ownWithdraws")
    @Expose
    private List<OwnWithdraw> ownWithdraws = null;

    public List<Deposit> getDeposits() {
        return deposits;
    }

    public void setDeposits(List<Deposit> deposits) {
        this.deposits = deposits;
    }

    public List<Withdraw> getWithdraws() {
        return withdraws;
    }

    public void setWithdraws(List<Withdraw> withdraws) {
        this.withdraws = withdraws;
    }

    public List<OwnDeposit> getOwnDeposits() {
        return ownDeposits;
    }

    public void setOwnDeposits(List<OwnDeposit> ownDeposits) {
        this.ownDeposits = ownDeposits;
    }

    public List<OwnWithdraw> getOwnWithdraws() {
        return ownWithdraws;
    }

    public void setOwnWithdraws(List<OwnWithdraw> ownWithdraws) {
        this.ownWithdraws = ownWithdraws;
    }
}
