package ai;

public enum AttackRule {

	ATTACK(1),
	NO_ATTACK(0);
	
	private int code;

	private AttackRule(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
	public static AttackRule getRule(int i) {
		for(AttackRule rule:values()) {
			if(rule.getCode() == i)
				return rule;
		}
		return null;
	}
	
}
