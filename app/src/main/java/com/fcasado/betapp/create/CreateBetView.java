package com.fcasado.betapp.create;

import com.fcasado.betapp.data.BetDataView;

/**
 * Created by fcasado on 6/14/16.
 */
public interface CreateBetView extends BetDataView {
	// react to bet created
	void onBetCreated(String error);


}
