package edu.epam.izhevsk.junit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.*;
import static org.mockito.Mockito.*;


public class PaymentControllerTest {
    @Mock
    AccountService accountService;
    @Mock
    DepositService depositService;
    @InjectMocks
    PaymentController paymentController;

    @Test
    @BeforeEach
    void testInitialMock(){
        MockitoAnnotations.initMocks(this);
        when(accountService.isUserAuthenticated(100L)).thenReturn(true);
        assertDoesNotThrow(()->when(depositService.deposit(lt(100L), any())).thenReturn("successfully"));
        assertDoesNotThrow(()->when(depositService.deposit(geq(100L), any())).thenThrow(InsufficientFundsException.class));
    }

    @Test
    void testSuccessfulDeposit(){
        assertDoesNotThrow(()->paymentController.deposit(50L,100L));
        verify(accountService,times(1)).isUserAuthenticated(100L);
    }

    @Test
    void testFailedDepositForUnauthenticatedUser(){
        assertThrows(SecurityException.class,()->paymentController.deposit(500L,10L));
    }

    @Test
    void failedDepositOfLargeAmount(){
        assertThrows(InsufficientFundsException.class,()->paymentController.deposit(500L,100L));
    }
}