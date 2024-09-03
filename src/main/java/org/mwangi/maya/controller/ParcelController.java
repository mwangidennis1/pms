package org.mwangi.maya.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.RandomStringUtils;
import org.mwangi.maya.dto.DateTimeDTO;
import org.mwangi.maya.dto.ParcelDTO;
import org.mwangi.maya.entities.*;
import org.mwangi.maya.exception.ResourceNotFoundException;
import org.mwangi.maya.services.ATService;
import org.mwangi.maya.services.ParcelSentGmailNotification;
import org.mwangi.maya.services.ParcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ParcelController {
    @Autowired
    private ParcelService parcelService;
    @Autowired
    private ATService atService;
    private ParcelSentGmailNotification parcelSentGmailNotification;

    public ParcelController(ParcelService parcelService, ParcelSentGmailNotification parcelSentGmailNotification) {
        this.parcelService = parcelService;
        this.parcelSentGmailNotification = parcelSentGmailNotification;
    }

    @GetMapping("add_new")
    public String showParcelForm(Model model){
        model.addAttribute("parcel",new ParcelDTO());
        return "create_parcel";
    }
    @PostMapping("/search")
    public  String getParcel(@RequestParam("trackingNumber") String trackingNumber,Model model){
        Parcel parcel =parcelService.getParcelByTrackNumber(trackingNumber);
        if(parcel == null){
            return "error";
        }
        model.addAttribute("parcel",parcel);
        return  "parcel-return";
    }
    @GetMapping("/dashboard")
    public String dashboard(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return "dashboard";
    }
    @GetMapping("/in_transit")
    public String getInTransit(Model model){
        List<Parcel> allParcels=parcelService.getParcels();

        List<Parcel> inTransitParcels = allParcels.stream()
                .filter(parcel -> parcel.getParcelStatus() == ParcelStatus.IN_TRANSIT)
                .collect(Collectors.toList());
        model.addAttribute("inTransitParcels", inTransitParcels);
        return  "in_transit";

    }
    @GetMapping("/delivered")
    public  String getDelivered(Model model){
        List<Parcel> allParcels=parcelService.getParcels();
        List<Parcel> deliveredParcels = allParcels.stream()
                .filter(parcel -> parcel.getParcelStatus() == ParcelStatus.DELIVERED)
                .collect(Collectors.toList());
        model.addAttribute("deliveredParcels", deliveredParcels);
        return "delivered";
    }
    @GetMapping("/returned")
    public String getReturned(Model model){
        List<Parcel> allParcels=parcelService.getParcels();
        List<Parcel> returnedParcels = allParcels.stream()
                .filter(parcel -> parcel.getParcelStatus() == ParcelStatus.RETURNED)
                .collect(Collectors.toList());
        model.addAttribute("returnedParcels", returnedParcels);
        return "returned";

    }
    @PostMapping ("/parcels")
    public String getParcelDetails(@ModelAttribute ParcelDTO parcelDTO, Model model, HttpSession session){
        Receiver receiver = new Receiver();
        receiver.setReceiverPhoneNo(parcelDTO.getReceiver().getReceiverPhoneNo());
        receiver.setReceiverEmail(parcelDTO.getReceiver().getReceiverEmail());
        Sender sender = new Sender();
        sender.setSenderName(parcelDTO.getSender().getSenderName());
        sender.setSenderPhoneNo(parcelDTO.getSender().getSenderPhoneNo());
        sender.setSenderEmail(parcelDTO.getSender().getSenderEmail());
        //String trackingNumber= RandomStringUtils.randomAlphanumeric(7);
        Parcel parcel =new Parcel();
        parcel.setDestination(parcelDTO.getDestination());
        parcel.setEstimatedDeliveryDate(parcelDTO.getEstimatedDeliveryDate());
        parcel.setApproxWeight(parcelDTO.getApproxWeight());
        parcel.setCategories(parcelDTO.getCategories());
        parcel.setParcelStatus(ParcelStatus.IN_TRANSIT);
        BigDecimal amount=calculateMoney(parcel);
        parcel.setAmount(amount);
        if(receiver.getReceiverEmail().isEmpty()){
            parcelService.setType(1);
        }else {
            parcelService.setType(0);
        }

        session.setAttribute("parcel", parcel);
        session.setAttribute("receiver", receiver);
        session.setAttribute("sender", sender);

        model.addAttribute("amount", amount);

        return "confirm-parcel";
    }
    @PostMapping("/confirm-parcel")
    public String confirmParcel(@RequestParam boolean accept, HttpSession session) {
        if (accept) {
            try {
                Parcel parcel = (Parcel) session.getAttribute("parcel");
                Receiver receiver = (Receiver) session.getAttribute("receiver");
                Sender sender = (Sender) session.getAttribute("sender");

                parcelService.createParcel(parcel, receiver, sender);

                // Clear session attributes
                session.removeAttribute("parcel");
                session.removeAttribute("receiver");
                session.removeAttribute("sender");

                return "redirect:/allparcels";
            } catch (IllegalArgumentException e) {
                return "error";
            }
        } else {
            // Clear session attributes
            session.removeAttribute("parcel");
            session.removeAttribute("receiver");
            session.removeAttribute("sender");
            return "redirect:/dashboard"; // or wherever you want to redirect if not accepted
        }
    }

    @GetMapping("/allparcels")
    public String parcels(Model model){
        List<Parcel> parcel=parcelService.getParcels();

        model.addAttribute("parcels",parcel);
          return "parcels";
    }
    @GetMapping("/tracking_parcels")
    public String trackParcels(){
        return "tracking_parcels";
    }
    @PostMapping("/tracking_parcels")
    public String getParcelTrackingInfo(@RequestParam("trackingNumber") String trackingNumber,Model model){
        Parcel parcel =parcelService.getParcelByTrackNumber(trackingNumber);
        if(parcel == null){
            return "error";
        }
        model.addAttribute("parcel",parcel);
        return  "tracking-return";
    }
        @GetMapping("/reports")
       public String getReport(Model model){
          model.addAttribute("datedto",new DateTimeDTO());
          return "reports";
        }
        @PostMapping("/reports")
        public  String showReport(@ModelAttribute DateTimeDTO dateTimeDTO,Model model){
         List<Parcel> parcel =parcelService.reportResults(dateTimeDTO.getFrom(),dateTimeDTO.getTo());
         model.addAttribute("parcels",parcel);
         return "report_result";
        }
    @GetMapping("/editParcel/{id}")
    public ModelAndView editParcel(@PathVariable(name = "id") long id) throws ResourceNotFoundException {
        Optional<Parcel> optionalParcel = parcelService.getByID(id);
        if (optionalParcel.isPresent()) {
            Parcel parcel = optionalParcel.get();
            return new ModelAndView("edit_parcel", "parcel", parcel);
        } else {
            throw new ResourceNotFoundException("Parcel not found with id: " + id);
        }
    }
      @PostMapping ("/parcelsedit")
      public  String editParci(@ModelAttribute("parcel") Parcel parcel){
        parcelService.saveParcel(parcel);
          return "redirect:/allparcels";
      }

      @GetMapping("/delete/{id}")
      public String deleteParci(@PathVariable(name = "id") long id){
        parcelService.deleteParcel(id);
        return "redirect:/allparcels";
      }
      @GetMapping("/ATLogs")
      public String ATLogs(Model model){
        model.addAttribute("balance",atService.initService());
        return "at_details";
      }
    @GetMapping("/parcels/pdf")
    public void generatePdfView(HttpServletResponse response) throws IOException, DocumentException {
        List<Parcel> parcels = parcelService.getParcels();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=maya_reports.pdf");

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        /*Add logo (replace with your actual logo path)
        //Image logo = Image.getInstance("path/to/your/logo.png");
        //logo.scaleToFit(100, 100);
        //logo.setAlignment(Element.ALIGN_LEFT);
        //document.add(logo);
          */
        // Add title
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, BaseColor.DARK_GRAY);
        Paragraph title = new Paragraph("Maya Reports", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingBefore(20);
        title.setSpacingAfter(20);
        document.add(title);

        // Create table
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Table header style
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        BaseColor headerBackground = new BaseColor(0, 102, 204); // Dark blue

        // Add table headers
        String[] headers = {"ID", "Destination", "Date of Delivery", "Weight", "Status"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBackgroundColor(headerBackground);
            cell.setPadding(8);
            table.addCell(cell);
        }

        // Alternating row colors
        BaseColor lightBlue = new BaseColor(237, 244, 252);
        BaseColor white = BaseColor.WHITE;
        Font contentFont = new Font(Font.FontFamily.HELVETICA, 10);

        // Add parcel data to table
        boolean alternateColor = false;
        for (Parcel parcel : parcels) {
            BaseColor rowColor = alternateColor ? lightBlue : white;
            addCell(table, parcel.getId().toString(), contentFont, rowColor);
            addCell(table, parcel.getDestination(), contentFont, rowColor);
            addCell(table, parcel.getEstimatedDeliveryDate().toString(), contentFont, rowColor);
            addCell(table, parcel.getApproxWeight().toString(), contentFont, rowColor);
            addCell(table, parcel.getParcelStatus().toString(), contentFont, rowColor);
            alternateColor = !alternateColor;
        }

        document.add(table);

        // Add footer
        Phrase footer = new Phrase("Page " + writer.getPageNumber(), new Font(Font.FontFamily.HELVETICA, 8));
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, footer,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom() - 10, 0);

        document.close();
    }

    private void addCell(PdfPTable table, String content, Font font, BaseColor color) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(color);
        cell.setPadding(5);
        table.addCell(cell);
    }
    private BigDecimal calculateMoney(Parcel parcel){
        double baseprice= 200;
        Categories c=parcel.getCategories();
        double w=Double.valueOf(parcel.getApproxWeight());
        BigDecimal price= BigDecimal.valueOf(0.0);
        switch (c){
            case FRAGILE -> price= BigDecimal.valueOf(baseprice + 200);
            case STANDARD -> price=BigDecimal.valueOf(baseprice+100);
            case HARZARDOUS -> price=BigDecimal.valueOf(baseprice+300);
            case PERISHABLE -> price=BigDecimal.valueOf(baseprice+250);
            default -> {
            }
        }
        return price;
    }

}
