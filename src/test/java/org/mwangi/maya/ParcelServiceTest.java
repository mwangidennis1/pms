package org.mwangi.maya;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mwangi.maya.entities.*;
import org.mwangi.maya.repositories.ParcelRepository;
import org.mwangi.maya.repositories.ReceiverRepository;
import org.mwangi.maya.repositories.SenderRepository;
import org.mwangi.maya.services.NotificationFactory;
import org.mwangi.maya.services.ParcelSentGmailNotification;
import org.mwangi.maya.services.ParcelService;
import org.mwangi.maya.utility.Notif;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParcelServiceTest {

    @Mock
    private ParcelRepository parcelRepository;
    @Mock
    private ReceiverRepository receiverRepository;
    @Mock
    private SenderRepository senderRepository;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
    @Mock
    private ParcelSentGmailNotification parcelSentGmailNotification;
    @Mock
    private NotificationFactory notificationFactory;
    @Mock
    private Notif notif;

    private ParcelService parcelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        parcelService = new ParcelService(applicationEventPublisher, parcelRepository, receiverRepository, senderRepository, parcelSentGmailNotification, notificationFactory);
    }

    @Test
    void testCreateParcel() {
        Parcel parcel = new Parcel();
        Receiver receiver = new Receiver();
        receiver.setReceiverEmail("test@example.com");
        Sender sender = new Sender();

        when(notificationFactory.getNotificationService(anyString())).thenReturn(notif);

        parcelService.setType(0); // Set to email notification
        parcelService.createParcel(parcel, receiver, sender);

        verify(receiverRepository).save(receiver);
        verify(senderRepository).save(sender);
        verify(parcelRepository).save(parcel);
        verify(notif).sendNotif(eq("test@example.com"), isNull(), anyString());
    }

    @Test
    void testGetParcels() {
        List<Parcel> expectedParcels = Arrays.asList(new Parcel(), new Parcel());
        when(parcelRepository.findAll()).thenReturn(expectedParcels);

        List<Parcel> actualParcels = parcelService.getParcels();

        assertEquals(expectedParcels, actualParcels);
    }

    @Test
    void testGetParcelByTrackNumber() {
        String trackingNumber = "ABC123";
        Parcel expectedParcel = new Parcel();
        when(parcelRepository.findParcelByTrackingNumber(trackingNumber)).thenReturn(expectedParcel);

        Parcel actualParcel = parcelService.getParcelByTrackNumber(trackingNumber);

        assertEquals(expectedParcel, actualParcel);
    }

    @Test
    void testUpdateParcelStatus() {
        String trackingNumber = "ABC123";
        Parcel parcel = new Parcel();
        Receiver receiver = new Receiver();
        receiver.setReceiverEmail("test@example.com");
        parcel.setReceiver(receiver);

        when(parcelRepository.findParcelByTrackingNumber(trackingNumber)).thenReturn(parcel);
        when(notificationFactory.getNotificationService(anyString())).thenReturn(notif);

        parcelService.setType(0); // Set to email notification
        parcelService.updateParcelStatus(trackingNumber);

        assertEquals(ParcelStatus.DELIVERED, parcel.getParcelStatus());
        verify(parcelRepository).save(parcel);
        verify(notif).sendNotif(eq("test@example.com"), isNull(), eq(trackingNumber));
    }

    @Test
    void testReportResults() {
        LocalDateTime from = LocalDateTime.now().minusDays(7);
        LocalDateTime to = LocalDateTime.now();
        List<Parcel> expectedParcels = Arrays.asList(new Parcel(), new Parcel());

        when(parcelRepository.findParcelByEstimatedDeliveryDateIsBetweenDate(from, to)).thenReturn(expectedParcels);

        List<Parcel> actualParcels = parcelService.reportResults(from, to);

        assertEquals(expectedParcels, actualParcels);
    }

    @Test
    void testGetByID() {
        long id = 1L;
        Parcel expectedParcel = new Parcel();
        when(parcelRepository.findById(id)).thenReturn(Optional.of(expectedParcel));

        Optional<Parcel> actualParcel = parcelService.getByID(id);

        assertTrue(actualParcel.isPresent());
        assertEquals(expectedParcel, actualParcel.get());
    }

    @Test
    void testDeleteParcel() {
        long id = 1L;
        parcelService.deleteParcel(id);
        verify(parcelRepository).deleteById(id);
    }

    @Test
    void testSaveParcel() {
        Parcel parcel = new Parcel();
        parcelService.saveParcel(parcel);
        verify(parcelRepository).save(parcel);
    }
}