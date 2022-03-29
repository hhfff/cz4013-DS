import struct

class Marshalling:


    @classmethod
    def marshall(cls,data: tuple) -> bytearray:
        '''marshall data to byte array'''      
        marshalled_data = bytearray()

        # convert every data element to bytes 
        for item in data:
            marshalled_data += cls.toBytes(item)            

        return marshalled_data

    @classmethod
    def unmarshall(cls,buffer: bytearray,paraTypeOrder:tuple) -> list:  
        '''Unmarshall bytearray to list of data element following the data element type list: paraTypeOrder'''            
        unmarshall_data = []
        ptr = 0 
        for data_type in paraTypeOrder:
            if data_type is int:
                unmarshall_data.append(struct.unpack('>i',buffer[ptr:ptr+4])[0])
                ptr +=4
            elif data_type is float:
                unmarshall_data.append(struct.unpack('>d',buffer[ptr:ptr+4])[0])
                ptr +=4
            elif data_type is str:
                length = struct.unpack('>i',buffer[ptr:ptr+4])[0]
                ptr +=4 
                unmarshall_data.append(buffer[ptr:ptr+length].decode('utf-8'))
                ptr +=length                

        return unmarshall_data

    @classmethod
    def toBytes(cls, data)-> bytes:  
        '''Convert a data to bytes based on the data type: int, float or string'''      
        # < little-endian
        # > big-endian

        if isinstance(data,int):
            return struct.pack('>i', data)
        elif isinstance(data,float):
            return struct.pack('>d', data)
        elif isinstance(data,str):
            # length of string is append before actual string content
            return struct.pack('>i', len(data)) + bytes(data.encode('utf-8'))
        else:
            raise Exception(f'Unsupported data type {type(data)} for marshalling')


# code for test purpose
if __name__ == '__main__':
    msg_byteArray = Marshalling.marshall((0,123,456.789,"abcdefg"))
    msg = Marshalling.unmarshall(msg_byteArray,(int,int,float,str))

    print(msg)