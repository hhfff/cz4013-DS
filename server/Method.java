/**
 * @author LONG
 * For all the service type with their integer value
 */
public enum Method {
    CREATE_ACCOUNT(1),
    CLOSE_ACCOUNT(2),
    DEPOSITE(3),
    WITHDRAW(4),
    VIEW_BALANCE(5),
    CURRENCY_EXCHANGE(6),
    MONITOR(7),
	USER_VERIFICATION(8);

    private int value;
    private Method(int value){
        this.value=value;
    }
    public int getValue(){
        return value;
    }
    public static Method getmethod(int value) {
		Method m = Method.CREATE_ACCOUNT;
    	switch(value) {
		case 1 : m = Method.CREATE_ACCOUNT;
				 break;
		case 2 : m = Method.CLOSE_ACCOUNT;
				 break;
		case 3 : m = Method.DEPOSITE;
				 break;
		case 4 : m = Method.WITHDRAW;
				 break;
		case 5 : m = Method.WITHDRAW;
				 break;
		case 6 : m = Method.WITHDRAW;
				 break;
		case 7 : m = Method.WITHDRAW;
				 break;
		case 8 : m =  Method.WITHDRAW;
				 break;
		
		}
		return m;
    	
    	
    }

}
