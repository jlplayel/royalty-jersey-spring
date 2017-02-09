package org.jlplayel.royalty.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jlplayel.royalty.model.Payment;
import org.springframework.jdbc.core.RowMapper;

public class PaymentRowMapper implements RowMapper<Payment> {

    @Override
    public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Payment payment = new Payment();
        payment.setRightsOwnerId(rs.getString("ID"));
        payment.setRightsOwner(rs.getString("NAME"));
        payment.setViewings(rs.getInt("TOTAL_VIEWING"));
        payment.setPaymentUnit(rs.getBigDecimal("PAYMENT_UNIT"));
        return payment;
    }



}
