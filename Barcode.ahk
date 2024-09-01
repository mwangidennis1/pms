#Requires AutoHotkey v2.0
SetWorkingDir A_ScriptDir  ; Ensures a consistent starting directory.

^!1::
{
SendBarcodeToSystem("nqlGocb")
}

SendBarcodeToSystem(barcode) {
    url := "http://localhost:8080/parcels/update-status"
    ; Properly format JSON payload
    postData := '{"barcode": "' . barcode . '"}'

    try {
        WebRequest := ComObject("WinHttp.WinHttpRequest.5.1")
        WebRequest.Open("POST", url)
        WebRequest.SetRequestHeader("Content-Type", "application/json")
        WebRequest.Send(postData)

        if (WebRequest.Status == 200) {
            MsgBox("Parcel status updated successfully for barcode: " . postData)
        } else {
            MsgBox("Error: " . WebRequest.Status . " - " . WebRequest.StatusText)

        }
    } catch as err {
        MsgBox("An error occurred: " . err.Message)
    }
}
