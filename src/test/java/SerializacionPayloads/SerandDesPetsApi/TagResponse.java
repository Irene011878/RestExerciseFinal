package SerializacionPayloads.SerandDesPetsApi;

public class TagResponse {

    private Long id;
    private String name;

    public TagResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public TagResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
