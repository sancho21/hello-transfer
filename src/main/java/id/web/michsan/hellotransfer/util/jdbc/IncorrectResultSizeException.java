package id.web.michsan.hellotransfer.util.jdbc;

public class IncorrectResultSizeException extends RuntimeException {
    public IncorrectResultSizeException(int expected, int factual) {
        super(String.format("Received result size %d while expecting it to be %d", factual, expected));
    }
}
