package courseworks.sockshop.model;

import courseworks.sockshop.model.exceptions.ReceiptException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
public class Sock {
    private Color color;
    private Size size;
    private int cottonPart;

    private int count;

    public Sock() {

    }

    public Sock(Color color, Size size, int cottonPart, int count) {
        this.color = color;
        this.size = size;

        if (cottonPart < 0 || cottonPart > 100) {
            throw new ReceiptException("Проверьте состав продукта");
        } else {
            this.cottonPart = cottonPart;
        }

        setCount(count);
    }

    public void setCount(int count) {
        if (count > 0) {
            this.count = count;
        } else {
            throw new ReceiptException("Некорректное количество товара");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sock sock = (Sock) o;
        return cottonPart == sock.cottonPart && color == sock.color && size == sock.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, size, cottonPart);
    }
}
