package kz.oneoiq.identity.domain.port.out;

public interface CodeGenerator {

    String generate();

    String hash(String code);

    boolean matches(String code, String hash);
}
