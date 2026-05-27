package org.igirepay.igirepaypaymentgatewayproject.LAB2.dao;

import org.igirepay.igirepaypaymentgatewayproject.LAB1.model.Customer;

public interface CustomerDAO {


        void addCustomer(Customer customer);

        Customer getCustomerById(int id);

        void viewCustomers();

        void updateCustomer(Customer customer);

        void deleteCustomer(int id);

        void incrementFailedAttempts(int customerId);

        void lockAccount(int customerId);

        void unlockAccount(int customerId);

        void resetFailedAttempts(int customerId);

}
