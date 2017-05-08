package edu.bigdata.elasticsearch;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

//@Getter @Setter
@XmlRootElement(name = "products")
public class Products {
    @XmlElement(name = "product")
    List<Product> productList = new ArrayList<>();
}
