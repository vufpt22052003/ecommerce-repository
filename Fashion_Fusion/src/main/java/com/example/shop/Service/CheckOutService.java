package com.example.shop.Service;

import com.example.shop.model.Address;

public interface CheckOutService {

	void adres(Address adr);

	Address getAdres(int uid);

	void updateAdres(Address adr);


}
