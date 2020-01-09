package biz.belcorp.consultoras.common.sms

class SMSEvent{

    var number : String? = null
    var body : String? = null

    constructor(number : String?, body : String?) {
        this.number = number
        this.body = body
    }

}
