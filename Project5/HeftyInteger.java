import java.util.Random;

public class HeftyInteger {

	private final byte[] ONE = {(byte) 1};
    private final byte[] ZERO = {(byte) 0};

	private byte[] val;

	/**
	 * Construct the HeftyInteger from a given byte array
	 * @param b the byte array that this HeftyInteger should represent
	 */
	public HeftyInteger(byte[] b) {
        val = b;
		
	}

	/**
	 * Return this HeftyInteger's val
	 * @return val
	 */
	public byte[] getVal() {
		return val;
	}

	/**
	 * Return the number of bytes in val
	 * @return length of the val byte array
	 */
	public int length() {
		return val.length;
	}

	/**
	 * Add a new byte as the most significant in this
	 * @param extension the byte to place as most significant
	 */
	public void extend(byte extension) {
		byte[] newv = new byte[val.length + 1];
		newv[0] = extension;
		for (int i = 0; i < val.length; i++) {
			newv[i + 1] = val[i];
		}
		val = newv;
	}

	/**
	 * If this is negative, most significant bit will be 1 meaning most
	 * significant byte will be a negative signed number
	 * @return true if this is negative, false if positive
	 */
	public boolean isNegative() {
		return (val[0] < 0);
	}

	/**
	 * Computes the sum of this and other
	 * @param other the other HeftyInteger to sum with this
	 */
	public HeftyInteger add(HeftyInteger other) {
		byte[] a, b;
		// If operands are of different sizes, put larger first ...
		if (val.length < other.length()) {
			a = other.getVal();
			b = val;
		}
		else {
			a = val;
			b = other.getVal();
		}


		// ... and normalize size for convenience
		if (b.length < a.length) {
			int diff = a.length - b.length;

			byte pad = (byte) 0;
			if (b[0] < 0) {
				pad = (byte) 0xFF;
			}

			byte[] newb = new byte[a.length];
			for (int i = 0; i < diff; i++) {
				newb[i] = pad;
			}

			for (int i = 0; i < b.length; i++) {
				newb[i + diff] = b[i];
			}

			b = newb;
		}

		// Actually compute the add
		int carry = 0;
		byte[] res = new byte[a.length];
		for (int i = a.length - 1; i >= 0; i--) {

			// Be sure to bitmask so that cast of negative bytes does not
			//  introduce spurious 1 bits into result of cast
			carry = ((int) a[i] & 0xFF) + ((int) b[i] & 0xFF) + carry;

			// Assign to next byte
			res[i] = (byte) (carry & 0xFF);

			// Carry remainder over to next byte (always want to shift in 0s)
			carry = carry >>> 8;
		}

		HeftyInteger res_li = new HeftyInteger(res);

		// If both operands are positive, magnitude could increase as a result
		//  of addition
		if (!this.isNegative() && !other.isNegative()) {
			// If we have either a leftover carry value or we used the last
			//  bit in the most significant byte, we need to extend the result
			if (res_li.isNegative()) {
				res_li.extend((byte) carry);
			}
		}
		// Magnitude could also increase if both operands are negative
		else if (this.isNegative() && other.isNegative()) {
			if (!res_li.isNegative()) {
				res_li.extend((byte) 0xFF);
			}
		}

		// Note that result will always be the same size as biggest input
		//  (e.g., -127 + 128 will use 2 bytes to store the result value 1)
		return res_li;
	}

	/**
	 * Negate val using two's complement representation
	 * @return negation of this
	 */
	public HeftyInteger negate() {
		byte[] neg = new byte[val.length];
		int offset = 0;

		// Check to ensure we can represent negation in same length
		//  (e.g., -128 can be represented in 8 bits using two's
		//  complement, +128 requires 9)
		if (val[0] == (byte) 0x80) { // 0x80 is 10000000
			boolean needs_ex = true;
			for (int i = 1; i < val.length; i++) {
				if (val[i] != (byte) 0) {
					needs_ex = false;
					break;
				}
			}
			// if first byte is 0x80 and all others are 0, must extend
			if (needs_ex) {
				neg = new byte[val.length + 1];
				neg[0] = (byte) 0;
				offset = 1;
			}
		}

		// flip all bits
		for (int i  = 0; i < val.length; i++) {
			neg[i + offset] = (byte) ~val[i];
		}

		HeftyInteger neg_li = new HeftyInteger(neg);

		// add 1 to complete two's complement negation
		return neg_li.add(new HeftyInteger(ONE));
	}

	/**
	 * Implement subtraction as simply negation and addition
	 * @param other HeftyInteger to subtract from this
	 * @return difference of this and other
	 */
	public HeftyInteger subtract(HeftyInteger other) {
		return this.add(other.negate());
	}

	/**
	 * Compute the product of this and other
	 * @param other HeftyInteger to multiply by this
	 * @return product of this and other
	 */
	public HeftyInteger multiply(HeftyInteger other) {
		// YOUR CODE HERE (replace the return, too...)
		byte[] a, b, c;

		// If operands are of different sizes, put larger first ...
		if (val.length < other.length()) {
			a = other.getVal();
			b = val;
		}
		else {
			a = val;
			b = other.getVal();
		}

        // if this is negative, negate and set boolean
        HeftyInteger heftyA = new HeftyInteger(a);
        boolean aPos = true;
		if (heftyA.isNegative()) {
            heftyA = heftyA.negate();
            aPos = false;
		}

		a = heftyA.getVal();


        // if other is negative, negate and set boolean
        HeftyInteger heftyB = new HeftyInteger(b);
        boolean bPos = true;
		if (heftyB.isNegative()) {
			//heftyB = heftyB.subtract(oneInt);
            heftyB = heftyB.negate();
            bPos = false;
		}

		b = heftyB.getVal();
		

        //set array size to be maximum
		c = new byte[(a.length + b.length) + 1];

		HeftyInteger cInt = new HeftyInteger(new byte[(a.length + b.length) + 1]);
		int count = 0;
		int x;
		HeftyInteger heftyC;

		for (int j = b.length - 1; j >= 0; j--) {				//for each byte of second multiplicand
			for (int k = 0; k < 8; k++) {						//for each shift to find individual bit
				x = (b[j] >> k) & 1;						//shift right to grab bit of second multiplicand
                
                //if x is 1, c is a
				if (x == 1) {

                    c = new byte[(a.length + b.length) + 1];
                    
					for (int i = a.length - 1; i >= 0; i--) {
						c[c.length - 1 - (a.length - 1 - i)] = a[i];
					}

					c = shiftLeft(c, count);

                    //convert to heftyInteger
					heftyC = new HeftyInteger(c);
				
					cInt = cInt.add(heftyC);
					
				}
                
                //if x is not 1, increment the shift amount
				count++;	
			}
		}

        //if only one is negative, result is negative
		if (aPos && !bPos || ( !aPos && bPos)) {
			cInt = cInt.negate();
			
		}
		return cInt;

	}


	public byte[] shiftLeft(byte[] C, int x) {

		int mod = x % 8;
		byte mask = (byte) ((1 << mod) - 1);
        int offset = (x / 8);
        byte src;
        byte dest;
        int index;

        //shift left by x bits
		for (int i = 0; i < C.length; i++) {
			index = i + offset;
			if (index >= C.length) {
				C[i] = 0;
			}
			else {
				src = C[index];
				dest = (byte) (src << mod);
				if (index + 1 < C.length) {
					dest |= C[index + 1] >>> (8 - mod) & mask;
				}
				C[i] = dest;
			}
        }
        //return the shifted array
		return C;
	}

	/**
	 * Run the extended Euclidean algorithm on this and other
	 * @param other another HeftyInteger
	 * @return an array structured as follows:
	 *   0:  the GCD of this and other
	 *   1:  a valid x value
	 *   2:  a valid y value
	 * such that this * x + other * y == GCD in index 0
	 */
	 public HeftyInteger[] XGCD(HeftyInteger other) {
        // YOUR CODE HERE (replace the return, too...)
        
		byte[] a, b;

        a = val;
        b = other.getVal();
        
        HeftyInteger dividend = new HeftyInteger(a);
		
		boolean zero = true;

        //if divisor is 0, return
		for (byte bit : b) {
			if (bit != 0) {
				zero = false;
			}
		}

		if (zero == true) {
			return new HeftyInteger[] {this, new HeftyInteger(ONE), new HeftyInteger(ZERO)};
        }
        
        //if either are negative, negate them to make them positive

		if (this.isNegative()) {
            dividend = this.negate();
		}

		if (other.isNegative()) {
            other = other.negate();
		}
        
        HeftyInteger[] vals = other.XGCD(dividend.mod(other));
		HeftyInteger d = vals[0];
		HeftyInteger x = vals[2];
		HeftyInteger div = this.divide(other)[0];
		HeftyInteger y = vals[1].subtract(div.multiply(x));
		return new HeftyInteger[] {d, x, y};

	 }
	 public HeftyInteger mod(HeftyInteger divisor) {
		return this.divide(divisor)[1]; 
	 }

	public HeftyInteger[] divide(HeftyInteger divisor) {


        byte[] dividendArray, divisorArray;
        dividendArray = val;
        divisorArray = divisor.getVal();

        //if dividend is less than divisor, return
        if ((this.subtract(divisor)).isNegative()) {
            return new HeftyInteger[] {new HeftyInteger(ZERO), this};
        }
        

		HeftyInteger result = new HeftyInteger(ZERO);
		HeftyInteger quotient = new HeftyInteger(ZERO);
        
        //set array lengths
        byte[] quotientArray = new byte[dividendArray.length];
        byte[] remainderArray = new byte[dividendArray.length];

        //set temp value to 256 to keep track of maximum
        byte[] tempArray = new byte[2];
        HeftyInteger tempValue = new HeftyInteger(tempArray);
        tempValue = tempValue.add(new HeftyInteger(ONE));
        tempArray = tempValue.getVal();
        tempArray = shiftLeft(tempArray, 8);
        tempValue = new HeftyInteger(tempArray);

        //intitalize HeftyIntegers
        HeftyInteger remainderInt = new HeftyInteger(ZERO);
        HeftyInteger partQuotient = new HeftyInteger(ZERO);
        byte[] quo;
        
        //go through dividend array
        for (int i = 0; i < dividendArray.length; i++) {
            remainderArray = shiftLeft(remainderArray, 8);      //shift left by 1 byte
            remainderInt = new HeftyInteger(remainderArray);    //put array into heftyInt
            byte[] next = {(byte) dividendArray[i]};            //grab next byte
            

            HeftyInteger nextB = new HeftyInteger(next);

            remainderArray[remainderArray.length - 1] = next[0];       //place it in remainderArray
            
            remainderInt = new HeftyInteger(remainderArray);        //convert to hefty

         

            //go through the maximum amt of 256
            for (HeftyInteger j = new HeftyInteger(ZERO); ((j.subtract(tempValue.add(new HeftyInteger(ONE)))).isNegative()); j = j.add(new HeftyInteger(ONE))) {
         
                result = divisor.multiply(j);
                
                //if it overpasses, subtract 1 and break
                if ((remainderInt.subtract(result)).isNegative()) {

                    partQuotient = j.subtract(new HeftyInteger(ONE));       //subtract 1
                    quo = partQuotient.getVal();
                    quotientArray[i] = quo[0];
                    quotient = new HeftyInteger(quotientArray);
                 
                    remainderInt = remainderInt.subtract((partQuotient.multiply(divisor)));
                    
                    remainderArray = remainderInt.getVal();

                    break;
                }


            }
        }
    
        return new HeftyInteger[] {quotient, remainderInt};
        
    }

	 
}
